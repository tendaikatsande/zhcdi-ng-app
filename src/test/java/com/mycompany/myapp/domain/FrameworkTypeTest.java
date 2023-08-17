package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrameworkTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrameworkType.class);
        FrameworkType frameworkType1 = new FrameworkType();
        frameworkType1.setId(1L);
        FrameworkType frameworkType2 = new FrameworkType();
        frameworkType2.setId(frameworkType1.getId());
        assertThat(frameworkType1).isEqualTo(frameworkType2);
        frameworkType2.setId(2L);
        assertThat(frameworkType1).isNotEqualTo(frameworkType2);
        frameworkType1.setId(null);
        assertThat(frameworkType1).isNotEqualTo(frameworkType2);
    }
}
