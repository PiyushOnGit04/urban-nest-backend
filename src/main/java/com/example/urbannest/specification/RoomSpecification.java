package com.example.urbannest.specification;

import com.example.urbannest.model.Room;
import com.example.urbannest.model.RoomType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static Specification<Room> filterRooms(
            String search,
            Double minRent,
            Double maxRent,
            RoomType roomType
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Only available rooms
            predicates.add(criteriaBuilder.equal(root.get("available"), true));

            // Search in title, description, city and locality
            if (search != null && !search.trim().isEmpty()) {

                String pattern = "%" + search.toLowerCase() + "%";

                Predicate title = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        pattern
                );

                Predicate description = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        pattern
                );

                Predicate city = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")),
                        pattern
                );

                Predicate locality = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("locality")),
                        pattern
                );

                predicates.add(
                        criteriaBuilder.or(title, description, city, locality)
                );
            }

            // Minimum Rent
            if (minRent != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("rent"), minRent)
                );
            }

            // Maximum Rent
            if (maxRent != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("rent"), maxRent)
                );
            }

            // Room Type
            if (roomType != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("roomType"), roomType)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}