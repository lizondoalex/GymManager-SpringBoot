package com.pm.memberservice;

import com.pm.memberservice.dto.MemberRequestDTO;
import com.pm.memberservice.dto.MemberResponseDTO;
import com.pm.memberservice.exceptions.MemberNotFoundException;
import com.pm.memberservice.grpc.BillingServiceGrpcClient;
import com.pm.memberservice.kafka.KafkaProducer;
import com.pm.memberservice.model.Member;
import com.pm.memberservice.repository.MemberRepository;
import com.pm.memberservice.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BillingServiceGrpcClient billingServiceGrpcClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private MemberService memberService;

    @Test
    void shouldRegisterNewMemberSuccessfully() {

        UUID fixedId = UUID.randomUUID();
        LocalDate birth = LocalDate.parse("1995-01-01");
        LocalDate register = LocalDate.parse("2025-01-01");

        Member savedMember = new Member("John Doe", "john@email.com", "123 main street", birth, register);
        savedMember.setId(fixedId);

        MemberRequestDTO request = new MemberRequestDTO("john@email.com", "2025-01-01", "1995-01-01", "123 main street", "John Doe");

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponseDTO result = memberService.createMember(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(fixedId.toString());
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@email.com");

        verify(memberRepository, times(1))
                .save(any(Member.class));
    }
    @Test
    void shouldDeleteMemberWhenExists() {
        UUID memberId = UUID.randomUUID();
        Member member = new Member("John Doe", "john@email.com", "Address",
                LocalDate.now(), LocalDate.now());
        member.setId(memberId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberResponseDTO result = memberService.deleteMember(memberId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId.toString());

        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    void shouldThrowExceptionWhenMemberToDeleteNotFound() {
        UUID memberId = UUID.randomUUID();

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.deleteMember(memberId);
        });

        verify(memberRepository, never()).delete(any(Member.class));
    }

    @Test
    void shouldUpdateMemberWhenExists(){
        UUID memberId = UUID.randomUUID();

        Member member = new Member("John Doe", "john@email.com", "Address",
                LocalDate.now(), LocalDate.now());
        member.setId(memberId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberRequestDTO request = new MemberRequestDTO("otherEmail@email.com", "2025-01-01", "1995-01-01", "123 other street", "othername");

        MemberResponseDTO result = memberService.updateMember(memberId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(memberId.toString());
        assertThat(result.getName()).isEqualTo("othername");
        assertThat(result.getEmail()).isEqualTo("otherEmail@email.com");

        verify(memberRepository, times(1))
                .save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenMemberToUpdateNotFound() {
        UUID memberId = UUID.randomUUID();

        MemberRequestDTO request = new MemberRequestDTO("otherEmail@email.com", "2025-01-01", "1995-01-01", "123 other street", "othername");

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.updateMember(memberId, request);
        });

        verify(memberRepository, never()).save(any(Member.class));

    }
}
