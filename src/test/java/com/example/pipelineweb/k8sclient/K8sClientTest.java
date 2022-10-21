package com.example.pipelineweb.k8sclient;

import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class K8sClientTest {

    public K8sClientTest() throws IOException {
        String path = "F:/CodeDemo/config";
        ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(path))).build();
        Configuration.setDefaultApiClient(client);
        log.info("init k8s client config...");
    }

    @Test
    public void buildServiceTest() throws ApiException {
        CoreV1Api core = new CoreV1Api();
        V1Service service = new V1ServiceBuilder()
//                .withKind("Service")
//                .withApiVersion("v1")
                .withMetadata(new V1ObjectMetaBuilder()
                        .withName("pipeline-web-two-svc")
                        .withNamespace("develop")
                        .build())
                .withSpec(new V1ServiceSpecBuilder()
                        .withType("NodePort")
                        .addToSelector("app","pipeline-web-two")
                        .withPorts(new V1ServicePortBuilder()
                                .withName("http-pipeline-web-two-svc")
                                .withProtocol("TCP")
                                .withPort(80)
                                .withTargetPort(new IntOrString(80))
                                .build())
                        .build())
                .build();
        V1Service v1Service = core.createNamespacedService("develop", service, null, null, null, null);
        log.info("build k8s V1Service：{}", v1Service);
    }

    @Test
    public void buildDeploymentTest() throws ApiException {
        AppsV1Api app = new AppsV1Api();
        V1Deployment deployment = new V1DeploymentBuilder()
//                .withKind("Deployment")
//                .withApiVersion("apps/v1")
                .withMetadata(new V1ObjectMetaBuilder()
                        .withName("pipeline-web-two")
                        .withNamespace("develop")
                        .addToLabels("app","pipeline-web-two")
                        .build())
                .withSpec(new V1DeploymentSpecBuilder()
                        .withReplicas(1)
                        .withSelector(new V1LabelSelectorBuilder().addToMatchLabels("app","pipeline-web-two").build())
                        .withTemplate(new V1PodTemplateSpecBuilder()
                                .withMetadata(new V1ObjectMetaBuilder().addToLabels("app","pipeline-web-two").build())
                                .withSpec(new V1PodSpecBuilder()
                                        .withVolumes(new V1VolumeBuilder()
                                                        .withName("volume-localtime")
                                                        .withNewHostPath()
                                                        .withPath("/etc/localtime")
                                                        .withType("")
                                                        .endHostPath()
                                                        .build(),
                                                new V1VolumeBuilder()
                                                        .withName("volume-configmap")
                                                        .withNewConfigMap()
                                                        .withName("pipeline-web-config")
                                                        .endConfigMap()
                                                        .build())
                                        .withContainers(new V1ContainerBuilder()
                                                        .withName("container")
                                                        .withImage("registry.cn-shenzhen.aliyuncs.com/develop-liuzp/pipeline-web:v1.1.50")
                                                        .withPorts(new V1ContainerPortBuilder()
                                                                .withName("http-80")
                                                                .withContainerPort(80)
                                                                .withProtocol("TCP")
                                                                .build())
                                                        .withVolumeMounts(new V1VolumeMountBuilder()
                                                                        .withName("volume-localtime")
                                                                        .withReadOnly(Boolean.TRUE)
                                                                        .withMountPath("/etc/localtime")
                                                                        .build(),
                                                                new V1VolumeMountBuilder()
                                                                        .withName("volume-configmap")
                                                                        .withReadOnly(Boolean.TRUE)
                                                                        .withMountPath("/config")
                                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
        V1Deployment v1Deployment = app.createNamespacedDeployment("develop", deployment, null, null, null, null);
        log.info("build k8s V1Deployment：{}", v1Deployment);
    }

    @Test
    public void buildPodTest() throws ApiException {
        CoreV1Api core = new CoreV1Api();
        V1Pod pod = new V1PodBuilder(true)
//                .withKind("Pod")
//                .withApiVersion("v1")

                //metadata
                .withNewMetadata()
                .withName("pipeline-web-two")
                .withNamespace("develop")
//                .addToLabels("app","pipeline-web-two")
                .endMetadata()

                //spec
                .withNewSpec()
                .addToVolumes(new V1VolumeBuilder()
                                .withName("volume-localtime")
                                .withNewHostPath()
                                .withPath("/etc/localtime")
                                .withType("")
                                .endHostPath()
                                .build(),
                        new V1VolumeBuilder()
                                .withName("volume-configmap")
                                .withNewConfigMap()
                                .withName("pipeline-web-config")
                                .endConfigMap()
                                .build())
                .withContainers(new V1ContainerBuilder()
                        .withName("container")
                        .withImage("registry.cn-shenzhen.aliyuncs.com/develop-liuzp/pipeline-web:v1.1.50")
                        .withPorts(new V1ContainerPortBuilder()
                                .withName("http-80")
                                .withContainerPort(80)
                                .withProtocol("TCP")
                                .build())
                        .addToVolumeMounts(new V1VolumeMountBuilder()
                                        .withName("volume-localtime")
                                        .withReadOnly(Boolean.TRUE)
                                        .withMountPath("/etc/localtime")
                                        .build(),
                                new V1VolumeMountBuilder()
                                        .withName("volume-configmap")
                                        .withReadOnly(Boolean.TRUE)
                                        .withMountPath("/config")
                                        .build())
                        .build())
                .endSpec()
                .build();

        V1Pod v1Pod = core.createNamespacedPod("develop", pod, null, null, null, null);
        log.info("build k8s V1Pod：{}", v1Pod);
    }
}
