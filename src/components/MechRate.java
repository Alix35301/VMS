package components;

/**
 * @author ali
 * @created_on 7/28/20
 */
public class MechRate {
    String id, mech_id, rate;

    public String getId() {
        return "MR"+id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMech_id() {
        return mech_id;
    }

    public void setMech_id(String mech_id) {
        this.mech_id = mech_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
