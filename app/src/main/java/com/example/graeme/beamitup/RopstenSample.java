package com.example.graeme.beamitup;

import com.typesafe.config.ConfigFactory;

import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

class RopstenSample extends BasicSample{
    abstract static class RopstenSampleConfig {
        final String config =
                "peer.discovery = {" +
                        "    enabled = true \n" +
                        "    ip.list = [" +
                        "        '94.242.229.4:40404'," +
                        "        '94.242.229.203:30303'" +
                        "    ]" +
                        "} \n" +
                        "peer.p2p.eip8 = true \n" +
                        "peer.networkId = 3 \n" +
                        "sync.enabled = true \n" +
                        "genesis = ropsten.json \n" +
                        "blockchain.config.name = 'ropsten' \n" +
                        "database.dir = database-ropstenSample";

        abstract RopstenSample sampleBean();

        @Bean
        public SystemProperties systemProperties() {
            SystemProperties props = new SystemProperties();
            props.overrideParams(ConfigFactory.parseString(config.replaceAll("'", "\"")));
            return props;
        }

    }
}
