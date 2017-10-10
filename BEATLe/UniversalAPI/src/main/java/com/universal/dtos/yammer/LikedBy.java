
package com.universal.dtos.yammer;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class LikedBy {

    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("names")
    @Expose
    private List<Object> names = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The count
     */
    public Long getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The names
     */
    public List<Object> getNames() {
        return names;
    }

    /**
     * 
     * @param names
     *     The names
     */
    public void setNames(List<Object> names) {
        this.names = names;
    }

}
