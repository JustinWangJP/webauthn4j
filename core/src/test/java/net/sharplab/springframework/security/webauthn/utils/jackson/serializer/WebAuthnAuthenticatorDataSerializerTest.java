/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sharplab.springframework.security.webauthn.utils.jackson.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sharplab.springframework.security.webauthn.attestation.authenticator.AbstractCredentialPublicKey;
import net.sharplab.springframework.security.webauthn.attestation.authenticator.ESCredentialPublicKey;
import net.sharplab.springframework.security.webauthn.attestation.authenticator.WebAuthnAttestedCredentialData;
import net.sharplab.springframework.security.webauthn.attestation.authenticator.WebAuthnAuthenticatorData;
import net.sharplab.springframework.security.webauthn.test.CoreTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static net.sharplab.springframework.security.webauthn.attestation.authenticator.WebAuthnAuthenticatorData.BIT_AT;
import static net.sharplab.springframework.security.webauthn.attestation.authenticator.WebAuthnAuthenticatorData.BIT_UP;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ynojima on 2017/08/18.
 */
public class WebAuthnAuthenticatorDataSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = CoreTestUtil.createCBORMapper();

        byte[] credentialId = "credentialId".getBytes(StandardCharsets.UTF_8);
        AbstractCredentialPublicKey credentialPublicKey = new ESCredentialPublicKey();

        byte[] aaGuid = new byte[16];

        byte[] rpIdHash = new byte[32];
        byte flags = (byte)(BIT_UP | BIT_AT);
        long counter = 325;

        WebAuthnAttestedCredentialData attestationData = new WebAuthnAttestedCredentialData();
        attestationData.setAaGuid(aaGuid);
        attestationData.setCredentialId(credentialId);
        attestationData.setCredentialPublicKey(credentialPublicKey);

        WebAuthnAuthenticatorData authenticatorData = new WebAuthnAuthenticatorData();
        authenticatorData.setRpIdHash(rpIdHash);
        authenticatorData.setFlags(flags);
        authenticatorData.setCounter(counter);
        authenticatorData.setAttestationData(attestationData);

        //Given

        //When
        byte[] result = objectMapper.writeValueAsBytes(authenticatorData);
        WebAuthnAuthenticatorData deserialized = objectMapper.readValue(result, WebAuthnAuthenticatorData.class);

        //Then

        assertThat(deserialized.getRpIdHash()).isEqualTo(rpIdHash);
        assertThat(deserialized.getFlags()).isEqualTo(flags);
        assertThat(deserialized.getCounter()).isEqualTo(counter);
        assertThat(deserialized.getAttestationData()).isNotNull();
        assertThat(deserialized.getAttestationData().getAaGuid()).isEqualTo(aaGuid);
        assertThat(deserialized.getAttestationData().getCredentialId()).isEqualTo(credentialId);
        assertThat(deserialized.getAttestationData().getCredentialPublicKey()).isEqualTo(credentialPublicKey);
        assertThat(deserialized.getExtensions()).isNull();
    }

}
