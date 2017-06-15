package com.talenttakeaways.kristaljewels.beans;

/**
 * Created by sanath on 14/06/17.
 */

public class Comments {
    private String commentId, commentAuthorName, commentMessage, commentAuthorId;

    public String getCommentId() {
        return commentId;
    }

    public String getCommentAuthorId() {
        return commentAuthorId;
    }

    public void setCommentAuthorId(String commentAuthorId) {
        this.commentAuthorId = commentAuthorId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentAuthorName() {
        return commentAuthorName;
    }

    public void setCommentAuthorName(String commentAuthorName) {
        this.commentAuthorName = commentAuthorName;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }
}
