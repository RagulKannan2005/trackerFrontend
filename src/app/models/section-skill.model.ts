export interface SectionSkillRequest {
  trackerSectionId: number;
  skillId: number;
  displayOrder: number;
}

export interface SectionSkillResponse {
  id: number;
  trackerSectionId: number;
  sectionName?: string;
  skillId: number;
  skillName: string;
  description: string;
  displayOrder: number;
}
