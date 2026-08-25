export interface Section {
    id: number;
    sectionName:string;
    description:string;
    active:boolean
    
}

export interface SectionRequest{
    sectionName:String,
    description:String,
    active:boolean
    
}

export interface TrackerSectionRequest{
    trackerId:number;
    sectionId:number;
    displayOrder:number;
}

export interface TrackerSectionResponse{
    id:number;
    trackerId:number;
    trackerName:string;
    sectionId:number;
    sectionName:string;
    displayOrder:number;

}