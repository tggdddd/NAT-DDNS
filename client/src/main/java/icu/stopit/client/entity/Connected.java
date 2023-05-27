package icu.stopit.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Connected {
    private String id;
    private String name;
    private String host;
    private boolean status;
}
