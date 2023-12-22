package fr.TabetSaidi.tp1Backend.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.TabetSaidi.tp1Backend.entity.Tour;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("tourName")
    private String tourName;
    @JsonProperty("description")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;
    @JsonProperty("deliveryPersonId")
    private Long deliveryPersonId;

    public static TourDto fromEntity(Tour tour) {
        TourDtoBuilder tourDtoBuilder = TourDto.builder()
                .id(tour.getId())
                .tourName(tour.getTourName())
                .description(tour.getDescription())
                .startDate(tour.getStartDate())
                .endDate(tour.getEndDate());

        if (tour.getDeliveryPerson() != null) {
            tourDtoBuilder.deliveryPersonId(tour.getDeliveryPerson().getId());
        } else {
            tourDtoBuilder.deliveryPersonId(null); // or any default value you prefer
        }

        return tourDtoBuilder.build();
    }

    public static Tour toEntity(TourDto tourDto) {
        Tour tour = new Tour();
        tour.setId(tourDto.getId());
        tour.setTourName(tourDto.getTourName());
        tour.setDescription(tourDto.getDescription());
        tour.setStartDate(tourDto.getStartDate());
        tour.setEndDate(tourDto.getEndDate());


        return tour;
    }
}
