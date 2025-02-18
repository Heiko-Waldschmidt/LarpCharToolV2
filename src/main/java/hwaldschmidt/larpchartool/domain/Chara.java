package hwaldschmidt.larpchartool.domain;

import jakarta.persistence.*;
import org.thymeleaf.expression.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The character/role you play.
 *
 * @author Heiko Waldschmidt
 */
@Entity
public class Chara {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column(unique=true, nullable=false)
    private String name;

    @OneToMany(mappedBy = "chara", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Visit> getVisits (){
        return visits;
    }

    public void setVisits(List<Visit> visits){
        this.visits = visits;
    }

    // I don't compare the visits here ... this is to complicated ... I can't use a simple equals because of an endless
    // loop. Need to compare Ids only and prevent that one Id can be added only once.
    // So I need to change the list to a sorted set first then when i want to make it save but I can't find the right
    // method for this now, which is working with every jpa implementation.
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Chara that = (Chara) other;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(version, that.version)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Chara{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", version=" + version +
                '}';
    }
}
