package icstar.kbdsi.apps.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="reminder_id")
    private long reminderId;

    @Column(name="email")
    private String email;

    @Column(name="description")
    private String description;

    //Ongoing, Done, Cancel
    @Column(name="status", columnDefinition = "varchar(255) default 'ongoing'")
    private String status;
//    @Temporal(TemporalType.DATE)
//    @CreatedDate
    @Column(name="schedule_date")
    private Date scheduleDate;

//    @Temporal(TemporalType.DATE)
//    @CreatedDate
    @Column(name="payment_date")
    private Date paymentDate;

    @Column(name="amount")
    private Integer amount;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name="createdAt")
    private Date createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name="updatedAt")
    private Date updatedAt;

    @Column(name="isDeleted", columnDefinition = "boolean default false")
    private boolean isDeleted;
    @Column(name="isRepeated", columnDefinition = "boolean default false")
    private boolean isRepeated;

    @Column(name="isSend", columnDefinition = "boolean default false")
    private boolean isSend;

    @Column(name="updatedBy")
    private String updatedBy;
    @Column(name="createdBy")
    private String createdBy;

    public Reminder() {
    }

    public Reminder(String email, String description, Integer amount, boolean isRepeated, String createdBy, String status, Date scheduleDate, Date paymentDate) {
        this.email = email;
        this.description = description;
        this.scheduleDate = scheduleDate;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.isRepeated = isRepeated;
        this.createdBy = createdBy;
        this.status = status;
    }

    public long getReminderId() {
        return reminderId;
    }

    public void setReminderId(long reminderId) {
        this.reminderId = reminderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId=" + reminderId +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", scheduleDate=" + scheduleDate +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                ", isRepeated=" + isRepeated +
                ", isSend=" + isSend +
                ", updatedBy='" + updatedBy + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
