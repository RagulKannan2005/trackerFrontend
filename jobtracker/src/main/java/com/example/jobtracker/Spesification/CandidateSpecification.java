package com.example.jobtracker.Spesification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.jobtracker.Dto.CandidateFilterRequest;
import com.example.jobtracker.Entity.Candidate;

import jakarta.persistence.criteria.Predicate;

public class CandidateSpecification {

    public static Specification<Candidate> filter(CandidateFilterRequest filter){

        return (root, query, cb)->{

            List<Predicate> predicates=new ArrayList<>();

            if(filter.getCandidateName()!=null &&
                    !filter.getCandidateName().isBlank()){

                predicates.add(
                        cb.like(
                                cb.lower(root.get("candidateName")),
                                "%" + filter.getCandidateName().toLowerCase() + "%"
                        )
                );
            }

            if(filter.getGraduationYear()!=null &&
                    !filter.getGraduationYear().isBlank()){

                predicates.add(
                        cb.equal(
                                root.get("graduationYear"),
                                filter.getGraduationYear()
                        )
                );
            }

            if(filter.getStream()!=null &&
                    !filter.getStream().isBlank()){

                predicates.add(
                        cb.equal(
                                root.get("stream"),
                                filter.getStream()
                        )
                );
            }

            if(filter.getDegree()!=null &&
                    !filter.getDegree().isBlank()){

                predicates.add(
                        cb.equal(
                                root.get("degree"),
                                filter.getDegree()
                        )
                );
            }

            if(filter.getPhone()!=null &&
                    !filter.getPhone().isBlank()){

                predicates.add(
                        cb.like(
                                root.get("phone"),
                                "%" + filter.getPhone() + "%"
                        )
                );
            }

            if(filter.getHaveLaptop()!=null){
                predicates.add(cb.equal(root.get("haveLaptop"), filter.getHaveLaptop()));
            }

            if(filter.getHaveInternet()!=null){
                predicates.add(cb.equal(root.get("haveInternet"), filter.getHaveInternet()));
            }

            if(filter.getHaveMobile()!=null){
                predicates.add(cb.equal(root.get("haveMobile"), filter.getHaveMobile()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };

    }

}