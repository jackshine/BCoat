
package com.universal.dtos.yammer;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Body {

    @SerializedName("parsed")
    @Expose
    private String parsed;
    @SerializedName("plain")
    @Expose
    private String plain;
    @SerializedName("rich")
    @Expose
    private String rich;

    /**
     * 
     * @return
     *     The parsed
     */
    public String getParsed() {
        return parsed;
    }

    /**
     * 
     * @param parsed
     *     The parsed
     */
    public void setParsed(String parsed) {
        this.parsed = parsed;
    }

    /**
     * 
     * @return
     *     The plain
     */
    public String getPlain() {
        return plain;
    }

    /**
     * 
     * @param plain
     *     The plain
     */
    public void setPlain(String plain) {
        this.plain = plain;
    }

    /**
     * 
     * @return
     *     The rich
     */
    public String getRich() {
        return rich;
    }

    /**
     * 
     * @param rich
     *     The rich
     */
    public void setRich(String rich) {
        this.rich = rich;
    }

}
