package com.project.animal.entity;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Species implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer speciesId;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,8}$")
    private String speciesName;


}
