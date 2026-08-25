export interface Meeting {
  id: number;

  eventId: string;

  summary: string;

  description: string;

  date: string;

  time: string;

  duration: string;

  participants: string;

  location: string;

  meetLink: string;

  htmlLink: string;

  startDateTime: string;

  endDateTime: string;

  status: string;
}

export interface MeetingRequest {
  summary: string;

  description: string;

  startDateTime: string;

  endDateTime: string;

  attendeeEmails: string[];
}
