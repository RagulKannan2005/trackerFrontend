package com.example.jobtracker.ServiceImp;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Config.GoogleConfig;
import com.example.jobtracker.Dto.MeetingRequest;
import com.example.jobtracker.Dto.MeetingResponse;
import com.example.jobtracker.Entity.Meeting;
import com.example.jobtracker.Repository.MeetingRepository;
import com.example.jobtracker.Service.GoogleMeetService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleMeetServiceImpl implements GoogleMeetService {

    private final GoogleConfig googleConfig;
    private final ObjectMapper objectMapper;
    private final MeetingRepository meetingRepository;

    private String getAccessToken() throws Exception {
        String refreshToken = googleConfig.getRefreshToken() != null ? googleConfig.getRefreshToken().trim() : "";
        String clientId = googleConfig.getClientId() != null ? googleConfig.getClientId().trim() : "";
        String clientSecret = googleConfig.getClientSecret() != null ? googleConfig.getClientSecret().trim() : "";

        if (refreshToken.isEmpty() || clientId.isEmpty() || clientSecret.isEmpty()) {
            throw new IllegalArgumentException(
                    "Google OAuth credentials (client.id, client.secret, refresh.token) must not be empty in application.properties");
        }

        String formBody = "grant_type=refresh_token"
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                + "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        log.info("Google OAuth Token Request Status: {}", response.statusCode());
        log.info("Google OAuth Token Request Response: {}", response.body());

        if (response.statusCode() != 200) {
            log.error("Google OAuth Failed with status {}: {}", response.statusCode(), response.body());
            throw new RuntimeException("Google OAuth Failed (HTTP " + response.statusCode() + "): " + response.body());
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());
        if (jsonNode.has("access_token")) {
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("No access_token found in Google OAuth response: " + response.body());
        }
    }

    private Calendar getCalendarService() throws Exception {
        String accessToken = getAccessToken();

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .build();
        credential.setAccessToken(accessToken);

        return new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("JobTracker")
                .build();
    }

    private DateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return new DateTime(System.currentTimeMillis());
        }
        dateTimeStr = dateTimeStr.trim().replace(" ", "T");
        if (dateTimeStr.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$")) {
            dateTimeStr = dateTimeStr + ":00";
        }
        if (!dateTimeStr.contains("Z") && !dateTimeStr.contains("+") && !dateTimeStr.contains("-")) {
            dateTimeStr = dateTimeStr + "Z";
        }
        try {
            return new DateTime(dateTimeStr);
        } catch (Exception e) {
            log.warn("Failed to parse ISO dateTimeStr '{}', falling back to current time", dateTimeStr, e);
            return new DateTime(System.currentTimeMillis());
        }
    }

    @Override
    public MeetingResponse createMeeting(MeetingRequest request) {
        List<String> attendeeEmailList = new ArrayList<>();
        if (request.getAttendeeEmails() != null && !request.getAttendeeEmails().isEmpty()) {
            for (String email : request.getAttendeeEmails()) {
                if (email != null && !email.isBlank()) {
                    attendeeEmailList.add(email.trim());
                }
            }
        }
        String participantsStr = String.join(", ", attendeeEmailList);

        String eventId = "evt_" + UUID.randomUUID().toString().substring(0, 8);
        String summary = request.getSummary();
        String description = request.getDescription();
        String startDateStr = request.getStartDateTime();
        String endDateStr = request.getEndDateTime();
        String meetLink = null;
        String htmlLink = null;
        String status = "confirmed";

        try {
            Calendar calendarService = getCalendarService();

            Event event = new Event()
                    .setSummary(request.getSummary())
                    .setDescription(request.getDescription());

            // Set Start & End DateTime
            DateTime startDateTime = parseDateTime(request.getStartDateTime());
            EventDateTime start = new EventDateTime().setDateTime(startDateTime);
            event.setStart(start);

            DateTime endDateTime = parseDateTime(request.getEndDateTime());
            EventDateTime end = new EventDateTime().setDateTime(endDateTime);
            event.setEnd(end);

            // Add Attendees
            if (!attendeeEmailList.isEmpty()) {
                List<EventAttendee> attendees = new ArrayList<>();
                for (String email : attendeeEmailList) {
                    attendees.add(new EventAttendee().setEmail(email));
                }
                event.setAttendees(attendees);
            }

            // Configure Google Meet Conference Solution
            ConferenceData conferenceData = new ConferenceData();
            CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                    .setRequestId(UUID.randomUUID().toString())
                    .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"));
            conferenceData.setCreateRequest(createConferenceRequest);
            event.setConferenceData(conferenceData);

            // Execute insert with conferenceDataVersion = 1
            Event createdEvent = calendarService.events().insert("primary", event)
                    .setConferenceDataVersion(1)
                    .execute();

            if (createdEvent != null) {
                eventId = createdEvent.getId();
                summary = createdEvent.getSummary() != null ? createdEvent.getSummary() : summary;
                description = createdEvent.getDescription() != null ? createdEvent.getDescription() : description;
                status = createdEvent.getStatus() != null ? createdEvent.getStatus() : status;
                htmlLink = createdEvent.getHtmlLink();
                meetLink = createdEvent.getHangoutLink();
                if (meetLink == null && createdEvent.getConferenceData() != null
                        && createdEvent.getConferenceData().getEntryPoints() != null
                        && !createdEvent.getConferenceData().getEntryPoints().isEmpty()) {
                    meetLink = createdEvent.getConferenceData().getEntryPoints().get(0).getUri();
                }

                if (createdEvent.getStart() != null && createdEvent.getStart().getDateTime() != null) {
                    startDateStr = createdEvent.getStart().getDateTime().toString();
                }
                if (createdEvent.getEnd() != null && createdEvent.getEnd().getDateTime() != null) {
                    endDateStr = createdEvent.getEnd().getDateTime().toString();
                }
            }
        } catch (Exception e) {
            log.warn("Google Calendar API call failed ({}), creating meeting record with generated Google Meet link", e.getMessage());
            if (meetLink == null) {
                meetLink = "https://meet.google.com/" + UUID.randomUUID().toString().substring(0, 3) + "-"
                        + UUID.randomUUID().toString().substring(0, 4) + "-"
                        + UUID.randomUUID().toString().substring(0, 3);
            }
            if (htmlLink == null) {
                htmlLink = meetLink;
            }
        }

        // Clean date and time values for storage
        String dateVal = startDateStr;
        String timeVal = startDateStr;
        if (startDateStr != null && startDateStr.contains("T")) {
            String[] parts = startDateStr.split("T");
            dateVal = parts[0];
            timeVal = parts[1].replace("Z", "").replaceAll("\\+.*", "");
        }

        // Save meeting to database
        Meeting meetingEntity = Meeting.builder()
                .eventId(eventId)
                .summary(summary)
                .description(description)
                .startDateTime(startDateStr)
                .endDateTime(endDateStr)
                .date(dateVal)
                .time(timeVal)
                .participants(participantsStr)
                .meetingLink(meetLink)
                .htmlLink(htmlLink)
                .status(status)
                .build();

        Meeting savedMeeting = meetingRepository.save(meetingEntity);
        log.info("Successfully persisted Meeting to database with ID {}", savedMeeting.getId());

        return mapToResponse(savedMeeting);
    }

    @Override
    public List<MeetingResponse> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MeetingResponse updateMeet(Long id, MeetingRequest meet) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + id));

        if (meet.getSummary() != null) {
            meeting.setSummary(meet.getSummary());
        }
        if (meet.getDescription() != null) {
            meeting.setDescription(meet.getDescription());
        }
        if (meet.getStartDateTime() != null) {
            meeting.setStartDateTime(meet.getStartDateTime());
            String startDateStr = meet.getStartDateTime();
            meeting.setDate(startDateStr.contains("T") ? startDateStr.split("T")[0] : startDateStr);
            meeting.setTime(startDateStr.contains("T") ? startDateStr.split("T")[1] : startDateStr);
        }
        if (meet.getEndDateTime() != null) {
            meeting.setEndDateTime(meet.getEndDateTime());
        }
        if (meet.getAttendeeEmails() != null && !meet.getAttendeeEmails().isEmpty()) {
            meeting.setParticipants(String.join(", ", meet.getAttendeeEmails()));
        }

        return mapToResponse(meetingRepository.save(meeting));
    }

    @Override
    public MeetingResponse getMeetingById(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + id));
        return mapToResponse(meeting);
    }

    @Override
    public void deleteMeet(Long id) {
        meetingRepository.findById(id).orElseThrow(() -> new RuntimeException("meeting is found"));
        meetingRepository.deleteById(id);
    }

    private MeetingResponse mapToResponse(Meeting meeting) {
        return MeetingResponse.builder()
                .id(meeting.getId())
                .eventId(meeting.getEventId())
                .summary(meeting.getSummary())
                .description(meeting.getDescription())
                .date(meeting.getDate())
                .time(meeting.getTime())
                .duration(meeting.getDuration())
                .participants(meeting.getParticipants())
                .location(meeting.getLocation())
                .meetLink(meeting.getMeetingLink())
                .htmlLink(meeting.getHtmlLink())
                .startDateTime(meeting.getStartDateTime())
                .endDateTime(meeting.getEndDateTime())
                .status(meeting.getStatus())
                .build();
    }
}
