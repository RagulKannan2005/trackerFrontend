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
        if (!dateTimeStr.contains("Z") && !dateTimeStr.contains("+")) {
            dateTimeStr = dateTimeStr + "Z";
        }
        return new DateTime(dateTimeStr);
    }

    @Override
    public MeetingResponse createMeeting(MeetingRequest request) {
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
            List<String> attendeeEmailList = new ArrayList<>();
            if (request.getAttendeeEmails() != null && !request.getAttendeeEmails().isEmpty()) {
                List<EventAttendee> attendees = new ArrayList<>();
                for (String email : request.getAttendeeEmails()) {
                    if (email != null && !email.isBlank()) {
                        String cleanEmail = email.trim();
                        attendees.add(new EventAttendee().setEmail(cleanEmail));
                        attendeeEmailList.add(cleanEmail);
                    }
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

            String meetLink = createdEvent.getHangoutLink();
            if (meetLink == null && createdEvent.getConferenceData() != null
                    && createdEvent.getConferenceData().getEntryPoints() != null
                    && !createdEvent.getConferenceData().getEntryPoints().isEmpty()) {
                meetLink = createdEvent.getConferenceData().getEntryPoints().get(0).getUri();
            }

            String startDateStr = createdEvent.getStart() != null && createdEvent.getStart().getDateTime() != null
                    ? createdEvent.getStart().getDateTime().toString()
                    : request.getStartDateTime();
            String endDateStr = createdEvent.getEnd() != null && createdEvent.getEnd().getDateTime() != null
                    ? createdEvent.getEnd().getDateTime().toString()
                    : request.getEndDateTime();
            String participantsStr = String.join(", ", attendeeEmailList);

            // Save meeting to database
            Meeting meetingEntity = Meeting.builder()
                    .eventId(createdEvent.getId())
                    .summary(createdEvent.getSummary())
                    .description(createdEvent.getDescription())
                    .startDateTime(startDateStr)
                    .endDateTime(endDateStr)
                    .date(startDateStr != null && startDateStr.contains("T") ? startDateStr.split("T")[0]
                            : startDateStr)
                    .time(startDateStr != null && startDateStr.contains("T") ? startDateStr.split("T")[1]
                            : startDateStr)
                    .participants(participantsStr)
                    .meetingLink(meetLink)
                    .htmlLink(createdEvent.getHtmlLink())
                    .status(createdEvent.getStatus())
                    .build();

            Meeting savedMeeting = meetingRepository.save(meetingEntity);
            log.info("Successfully persisted Meeting to database with ID {}", savedMeeting.getId());

            return mapToResponse(savedMeeting);

        } catch (Exception e) {
            log.error("Failed to create Google Meet meeting: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Google Meet meeting: " + e.getMessage(), e);
        }
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
