package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cso.class);
        Cso cso1 = new Cso();
        cso1.setId(1L);
        Cso cso2 = new Cso();
        cso2.setId(cso1.getId());
        assertThat(cso1).isEqualTo(cso2);
        cso2.setId(2L);
        assertThat(cso1).isNotEqualTo(cso2);
        cso1.setId(null);
        assertThat(cso1).isNotEqualTo(cso2);
    }
}
