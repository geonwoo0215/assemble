package com.geonwoo.assemble.domain.imageurl.repository;

import com.geonwoo.assemble.domain.expense.model.Expense;
import com.geonwoo.assemble.domain.expense.repository.ExpenseJdbcRepository;
import com.geonwoo.assemble.domain.imageurl.model.ImageUrl;
import com.geonwoo.assemble.domain.party.model.Party;
import com.geonwoo.assemble.domain.party.repository.PartyJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@JdbcTest
@Sql("classpath:schema.sql")
class ImageUrlRepositoryTest {

    ImageUrlJdbcRepository imageUrlRepository;

    PartyJdbcRepository partyJdbcRepository;

    ExpenseJdbcRepository expenseJdbcRepository;

    @Autowired
    DataSource dataSource;

    Long partyId;
    Long expenseId;

    @BeforeEach
    void setUp() {
        imageUrlRepository = new ImageUrlJdbcRepository(dataSource);
        partyJdbcRepository = new PartyJdbcRepository(dataSource);
        expenseJdbcRepository = new ExpenseJdbcRepository(dataSource);

        Party party = new Party("name", "content", LocalDate.now());
        partyId = partyJdbcRepository.save(party);

        Expense expense = new Expense(partyId, 1000, "content");
        expenseId = expenseJdbcRepository.save(expense);
    }

    @Test
    void 이미지_저장_성공() {
        ImageUrl imageUrl = new ImageUrl(partyId, expenseId, "url");
        Long id = imageUrlRepository.save(imageUrl);
        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void 비용아이디로_이미지_조회_성공() {

        List<ImageUrl> imageUrls = IntStream.range(0, 5)
                .mapToObj(i -> {
                    ImageUrl imageUrl = new ImageUrl(partyId, expenseId, "url" + i);
                    imageUrlRepository.save(imageUrl);
                    return imageUrl;
                })
                .toList();

        List<String> imageUrlList = imageUrlRepository.findByExpenseId(expenseId);

        Assertions.assertThat(imageUrlList).hasSize(imageUrls.size());
    }

    @Test
    void 모임_아이디로_이미지_조회_성공() {

        List<ImageUrl> imageUrls = IntStream.range(0, 5)
                .mapToObj(i -> {
                    ImageUrl imageUrl = new ImageUrl(partyId, expenseId, "url" + i);
                    imageUrlRepository.save(imageUrl);
                    return imageUrl;
                })
                .toList();

        List<String> imageUrlList = imageUrlRepository.findByPartyId(partyId);

        Assertions.assertThat(imageUrlList).hasSize(imageUrls.size());
    }
}