export interface Skill {
  id: number;
  skillName: string;
  description: string;
  active: boolean;
}

export interface SkillsRequest {
    skillName:string;
    description:string;
    active:boolean;
}