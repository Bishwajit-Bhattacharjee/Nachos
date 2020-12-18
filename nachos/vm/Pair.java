package nachos.vm;

import java.util.Objects;

public class Pair {

    public Pair(Integer vpn, Integer pid) {
        this.vpn = vpn;
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "vpn=" + vpn +
                ", pid=" + pid +
                '}';
    }

    public Integer getPid() {
        return pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return vpn.equals(pair.vpn) && pid.equals(pair.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vpn, pid);
    }

    private Integer vpn;
    private Integer pid;
}
