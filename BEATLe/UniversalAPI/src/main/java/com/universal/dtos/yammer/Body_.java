
package com.universal.dtos.yammer;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Body_ {

    @SerializedName("plain")
    @Expose
    private String plain;

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

}
