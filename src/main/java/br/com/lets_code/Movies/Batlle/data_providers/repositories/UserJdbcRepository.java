package br.com.lets_code.Movies.Batlle.data_providers.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.lets_code.Movies.Batlle.presenter.rest.dtos.response.PlayerScore;

@Repository
public class UserJdbcRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    public List<PlayerScore> listPlayerScore() {
        String query = """
            SELECT  USR.ID AS id,
                    USR.USERNAME AS username,
                    USR.EMAIL AS email,
                    ROUND((100.00 * NULLIF(
                            (SELECT COUNT(*)
                            FROM USER USR_F
                            INNER JOIN GAME_MATCH GM ON USR.ID = GM.USER_ID
                            INNER JOIN GAME_ROUND GR ON GR.GAME_MATCH_ID = GM.ID
                            WHERE GR.SCORE = 1
                            AND USR_F.ID = USR.ID),
                        0)) / 
                        NULLIF(
                            (SELECT COUNT(*)
                            FROM USER USR_T
                            INNER JOIN GAME_MATCH GM ON USR_T.ID = GM.USER_ID
                            INNER JOIN GAME_ROUND GR ON GR.GAME_MATCH_ID = GM.ID
                            WHERE USR_T.ID = USR.ID),
                        0), 2) AS successRate
            FROM USER USR
            ORDER BY successRate DESC
        """;

        List<PlayerScore> playerScores = jdbcTemplate.query(query,
                (rs, rowNum) -> new PlayerScore(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        String.valueOf(rs.getDouble("successRate"))+"%"));
        return playerScores;
    }
    
}
