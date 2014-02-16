package am.ik.categolj2.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(columnList = "METHOD"),
        @Index(columnList = "URI"),
        @Index(columnList = "X_TRACK"),
        @Index(columnList = "ACCESS_DATE")
})
public class AccessLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Size(min = 36, max = 36)
    @Column(name = "ACCESS_LOG_ID")
    private String accessLogId;

    @Column(name = "METHOD")
    @NotNull
    @Size(min = 1, max = 10)
    private String method;

    @Column(name = "URI")
    @NotNull
    @Size(min = 1, max = 128)
    private String uri;

    @Column(name = "QUERY")
    @Size(min = 1, max = 128)
    private String query;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "REMOTE_ADDRESS")
    private String remoteAddress;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "USER_AGENT", length = 128)
    private String userAgent;

    @Size(min = 32, max = 32)
    @Column(name = "X_TRACK")
    private String xTrack;

    @Column(name = "ACCESS_DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime accessDate;

}
