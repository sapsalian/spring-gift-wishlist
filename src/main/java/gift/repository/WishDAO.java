package gift.repository;

import gift.dto.WishCreateDTO;
import gift.dto.WishInfoDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class WishDAO {
    private final JdbcTemplate jdbcTemplate;

    public WishDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WishInfoDTO> findWishes(long userId) {
        String sql = "SELECT * FROM wish WHERE user_id = ?";

        return jdbcTemplate.query(sql, (wishRecord, rowNum) -> new WishInfoDTO(
                wishRecord.getLong("id"),
                wishRecord.getLong("user_id"),
                wishRecord.getLong("product_id"),
                wishRecord.getInt("quantity")
        ), userId);
    }

    public WishInfoDTO create(WishCreateDTO wishCreateDTO) {
        long id = insertWithGeneratedKey(wishCreateDTO.userId(), wishCreateDTO.productId(), wishCreateDTO.quantity());

        return new WishInfoDTO(
                id,
                wishCreateDTO.userId(),
                wishCreateDTO.productId(),
                wishCreateDTO.quantity()
        );
    }

    private long insertWithGeneratedKey(long userId, long productId, int quantity) {
        String insertSql = "insert into wishes (user_id, product_id, quantity) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);

            return ps;
        }, keyHolder);

        return (long) keyHolder.getKey();
    }
}