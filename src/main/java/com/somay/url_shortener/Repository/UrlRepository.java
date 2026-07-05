package com.somay.url_shortener.Repository;

import com.somay.url_shortener.entity.ShortUrl;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl,Long> {
    @Query(
            value = """
                    SELECT * FROM short_url WHERE short_code = :shortCode
                    """,
            nativeQuery = true
    )
    Optional<ShortUrl> findByCd(
            @Param("shortCode") String shortCode
    );

    @Query(
            value = """
                    SELECT short_code FROM short_url WHERE original_url=:originalUrl
                    """,
            nativeQuery = true
    )
    Optional<String> findByURL(
            @Param("originalUrl") String originalUrl
    );

    @Modifying
    @Transactional
    @Query(
            value = """
                    UPDATE short_url SET click_count = click_count+1 WHERE short_code = :shortCode
                    """,
            nativeQuery = true
    )
    void incrementClickCount(
            @Param("shortCode") String shortCode
    );

    @Modifying
    @Transactional
    @Query(
            value = """
                    UPDATE short_url SET click_count = click_count+:count WHERE short_code = :shortCode
                    """,
            nativeQuery = true
    )
    void addClickCount(@Param("shortCode") String shortCode,@Param("count") Long count);
}
