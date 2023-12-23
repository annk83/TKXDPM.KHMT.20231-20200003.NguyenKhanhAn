package repo.impl;

import domain.IProvinceDomain;
import lombok.Builder;
import lombok.Getter;
import repo.IProvinceRepo;
import utils.ShippingConfig;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLProvinceRepo implements IProvinceRepo {
    private final SimpleConnection simpleConnection;
    public SQLProvinceRepo(SimpleConnection simpleConnection) {
        this.simpleConnection = simpleConnection;
    }

    @Override
    public List<IProvinceDomain> getAllProvince() {
        try {
            return simpleConnection.doStuff(
                    conn-> {
                        try{
                            var ret = new ArrayList<IProvinceDomain>();
                            var rs = conn.createStatement().executeQuery("" +
                                    "SELECT id, name, free_threadhold, lowest, start_price, extend_price, rush_charge " +
                                    "FROM province p LEFT JOIN ship_policy sp ON p.policy_id = sp.id");
                            while(rs.next()) {
                                var b = ProvinceDomainReadOnly.builder();
                                b.provinceId = rs.getInt(1);
                                b.name = rs.getString(2);
                                int i;
                                for(i=3;i<=7;++i) {
                                    rs.getLong(i);
                                    if(rs.wasNull()) break;
                                }
                                if(i<=7) b.shippingPolicy = null;
                                else {
                                    b.shippingPolicy = new ShippingConfig(rs.getLong(3), rs.getLong(4), rs.getLong(5), rs.getLong(6), rs.getLong(7));
                                }
                                ret.add(b.build());
                            }
                            return ret;
                        } catch (SQLException exception) {
                            System.err.println(exception.getMessage());
                            exception.printStackTrace(System.err);
                            return List.of();
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @Builder
    public static class ProvinceDomainReadOnly implements IProvinceDomain {
        private final int provinceId;
        private final String name;
        private final ShippingConfig shippingPolicy;
        public Optional<ShippingConfig> getShippingPolicy() {
            return Optional.ofNullable(shippingPolicy);
        }
    }
}
