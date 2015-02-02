package com.kickstartlab.android.mmm.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 12/15/14.
 */
public class Feed extends SugarRecord<Feed> {

    private int utime;
    private String type;
    private String message;
    private String mediaId;
    private String originatorId;
    private String mediaTitle;
    private String mediaType;
    private String mediaUrl;
    private String coverUrl;
    private String originatorName;
    private String originatorAvatar;
    private String updatedAt;
    private String createdAt;
    private String extId;


    public String toString(){
        return message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOriginatorAvatar() {
        return originatorAvatar;
    }

    public void setOriginatorAvatar(String originatorAvatar) {
        this.originatorAvatar = originatorAvatar;
    }

    /**
     *
     * @return
     * The utime
     */
    public int getUtime() {
        return utime;
    }

    /**
     *
     * @param utime
     * The utime
     */
    public void setUtime(int utime) {
        this.utime = utime;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The mediaId
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     *
     * @param mediaId
     * The mediaId
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    /**
     *
     * @return
     * The originatorId
     */
    public String getOriginatorId() {
        return originatorId;
    }

    /**
     *
     * @param originatorId
     * The originatorId
     */
    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    /**
     *
     * @return
     * The mediaTitle
     */
    public String getMediaTitle() {
        return mediaTitle;
    }

    /**
     *
     * @param mediaTitle
     * The mediaTitle
     */
    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    /**
     *
     * @return
     * The mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     *
     * @param mediaType
     * The mediaType
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     *
     * @return
     * The mediaUrl
     */
    public String getMediaUrl() {
        return mediaUrl;
    }

    /**
     *
     * @param mediaUrl
     * The mediaUrl
     */
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    /**
     *
     * @return
     * The coverUrl
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     *
     * @param coverUrl
     * The coverUrl
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /**
     *
     * @return
     * The originatorName
     */
    public String getOriginatorName() {
        return originatorName;
    }

    /**
     *
     * @param originatorName
     * The originatorName
     */
    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The extId
     */
    public String getExtId() {
        return extId;
    }

    /**
     *
     * @param extId
     * The extId
     */
    public void setExtId(String extId) {
        this.extId = extId;
    }


}
