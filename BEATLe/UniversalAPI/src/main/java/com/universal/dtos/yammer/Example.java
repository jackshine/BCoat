
package com.universal.dtos.yammer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Example {

    @SerializedName("threaded_extended")
    @Expose
    private ThreadedExtended threadedExtended;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = new ArrayList<Message>();
    @SerializedName("references")
    @Expose
    private List<Reference> references = new ArrayList<Reference>();
    @SerializedName("external_references")
    @Expose
    private List<Object> externalReferences = new ArrayList<Object>();
    @SerializedName("meta")
    @Expose
    private Meta meta;

    /**
     * 
     * @return
     *     The threadedExtended
     */
    public ThreadedExtended getThreadedExtended() {
        return threadedExtended;
    }

    /**
     * 
     * @param threadedExtended
     *     The threaded_extended
     */
    public void setThreadedExtended(ThreadedExtended threadedExtended) {
        this.threadedExtended = threadedExtended;
    }

    /**
     * 
     * @return
     *     The messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * 
     * @param messages
     *     The messages
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * 
     * @return
     *     The references
     */
    public List<Reference> getReferences() {
        return references;
    }

    /**
     * 
     * @param references
     *     The references
     */
    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    /**
     * 
     * @return
     *     The externalReferences
     */
    public List<Object> getExternalReferences() {
        return externalReferences;
    }

    /**
     * 
     * @param externalReferences
     *     The external_references
     */
    public void setExternalReferences(List<Object> externalReferences) {
        this.externalReferences = externalReferences;
    }

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
