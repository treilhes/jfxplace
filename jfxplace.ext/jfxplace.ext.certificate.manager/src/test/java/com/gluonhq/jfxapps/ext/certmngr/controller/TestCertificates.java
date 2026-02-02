package com.gluonhq.jfxapps.ext.certmngr.controller;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class TestCertificates {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private TestCertificates() {}

    public static KeyStore createCA(File file, char[] password) throws Exception {
        KeyPair keyPair = generateKeyPair();
        X509Certificate cert = generateCert(
            "CN=Test CA",
            keyPair,
            null,
            null,
            true
        );

        return writeKeystore(file, password, "ca", keyPair.getPrivate(), cert);
    }

    public static KeyStore createServer(
            File file,
            char[] password,
            PrivateKey caKey,
            X509Certificate caCert
    ) throws Exception {

        KeyPair serverKey = generateKeyPair();
        X509Certificate cert = generateCert(
            "CN=localhost",
            serverKey,
            caKey,
            caCert,
            false
        );

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, password);
        ks.setKeyEntry(
            "server",
            serverKey.getPrivate(),
            password,
            new X509Certificate[]{cert, caCert}
        );

        try (FileOutputStream fos = new FileOutputStream(file)) {
            ks.store(fos, password);
        }
        return ks;
    }

    // ----------------------------------------------------------------

    private static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        return gen.generateKeyPair();
    }

    private static X509Certificate generateCert(
            String dn,
            KeyPair subjectKey,
            PrivateKey issuerKey,
            X509Certificate issuerCert,
            boolean isCA
    ) throws Exception {

        long now = System.currentTimeMillis();
        Date from = new Date(now);
        Date to = new Date(now + 365L * 86400000);

        X500Name subject = new X500Name(dn);
        X500Name issuer = issuerCert == null
                ? subject
                : new X500Name(issuerCert.getSubjectX500Principal().getName());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .build(issuerKey == null ? subjectKey.getPrivate() : issuerKey);

        X509v3CertificateBuilder builder =
            new JcaX509v3CertificateBuilder(
                issuer,
                BigInteger.valueOf(now),
                from,
                to,
                subject,
                subjectKey.getPublic()
            );

        if (isCA) {
            builder.addExtension(
                org.bouncycastle.asn1.x509.Extension.basicConstraints,
                true,
                new org.bouncycastle.asn1.x509.BasicConstraints(true)
            );
        }

        X509CertificateHolder holder = builder.build(signer);
        return new JcaX509CertificateConverter()
                .getCertificate(holder);
    }

    private static KeyStore writeKeystore(
            File file,
            char[] password,
            String alias,
            PrivateKey key,
            X509Certificate cert
    ) throws Exception {

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, password);
        ks.setKeyEntry(alias, key, password, new X509Certificate[]{cert});

        try (FileOutputStream fos = new FileOutputStream(file)) {
            ks.store(fos, password);
        }
        return ks;
    }
}

