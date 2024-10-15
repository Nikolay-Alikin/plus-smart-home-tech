package app.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "conditions")
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "operation", length = Integer.MAX_VALUE)
    private String operation;

    @Column(name = "value")
    private Integer value;

    @OneToMany(mappedBy = "condition", cascade = CascadeType.REMOVE)
    private Set<ScenarioCondition> scenarioConditions = new LinkedHashSet<>();

}
