package in.stonecolddev.bocean.video;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.SqlParameterSourceFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.MimeType;

import java.util.List;
import java.util.Map;

@Repository
public class VideoRepositoryImpl implements VideoRepository {

    private final NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport;
    private final SqlParameterSourceFactory<Video> parameterSourceFactory;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Video> rowMapper =
            JdbcTemplateMapperFactory
                    .newInstance()
                    .ignorePropertyNotFound()
                    .addKeys("id")
                    .addGetterForType(
                        MimeType.class,
                        (rs, i) -> {
                            String val = rs.getString(i);
                            if (val != null)
                                return MimeType.valueOf(val);
                            return null;
                        }
                    )
                    .newRowMapper(Video.class);

    public VideoRepositoryImpl(NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport) {
        this.namedParameterJdbcDaoSupport = namedParameterJdbcDaoSupport;

        this.parameterSourceFactory =
                JdbcTemplateMapperFactory
                        .newInstance().newSqlParameterSourceFactory(Video.class);

        this.jdbcTemplate = this.namedParameterJdbcDaoSupport.getNamedParameterJdbcTemplate();

    }

    public List<Video> retrieve(int lastSeen, int pageSize) {
        return jdbcTemplate.query(
                """
                        select * from videos
                        where id > :lastSeen
                        order by created_on desc
                        limit :pageSize
                        """,
                Map.of("lastSeen", lastSeen,
                        "pageSize", pageSize
                ),
                rowMapper
        );
    }

    public void create(Video video) {

    }
}