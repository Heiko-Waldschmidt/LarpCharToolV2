package hwaldschmidt.larpchartool.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * The convention you visited.
 *
 * @author Heiko Waldschmidt
 */
@Entity
public class Convention {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "convention", cascade = CascadeType.ALL)
    private List<Visit> visits;

    @Column(unique=true, nullable=false)
    private String title;

    // Creates a converter for string to localeDate conversion. Needed since I use LocalDate
    @Column(nullable=false)
    private LocalDate start;

    // Creates a converter for string to localeDate conversion. Needed since I use LocalDate
    @Column(nullable=false)
    private LocalDate finish; // "end" is a reserved word in h2 databases

    private boolean df = false;

    public boolean isDf() {
        return df;
    }

    public void setDf(boolean df) {
        this.df = df;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    // I don't compare the visits here ... this is to complicated ... I can't use a simple equals because of an endless
    // loop. Need to compare Ids only and prevent that one Id can be added only once.
    // So I need to change the list to a sorted set first then when i want to make it save but I can't find the right
    // method for this now, which is working with every jpa implementation.
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Convention that = (Convention) other;

        if (df != that.df) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(version, that.version)) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(start, that.start)) return false;
        return Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (finish != null ? finish.hashCode() : 0);
        result = 31 * result + (df ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Convention{" +
                "id=" + id +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + finish +
                ", df=" + df +
                '}';
    }
}
