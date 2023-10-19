package icstar.kbdsi.apps.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="transaction_id")
    private long transactionId;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;


//    @ManyToOne
//    @JoinColumn(name="category", nullable=false)
//    private Category category;
    @Column(name="category")
    private String category;

    @Column(name="amount")
    private Integer amount;

    @Column(name="description", length = 255)
    private String description;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name="createdAt")
    private Date createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name="updatedAt")
    private Date updatedAt;

    @Column(name="isDeleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column(name="updatedBy")
    private String updatedBy;

    @Column(name="createdBy")
    private String createdBy;


    public Transaction() {
    }

    public Transaction(String name, String type, String category, Integer amount, String description, String createdBy) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.createdBy= createdBy;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
