package com.github.nagyesta.lowkeyvault.mapper.v7_3.certificate;

import com.github.nagyesta.lowkeyvault.mapper.AliasAwareConverter;
import com.github.nagyesta.lowkeyvault.model.v7_3.certificate.*;
import com.github.nagyesta.lowkeyvault.service.certificate.ReadOnlyKeyVaultCertificateEntity;
import com.github.nagyesta.lowkeyvault.service.certificate.impl.CertificateCreationInput;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Supplier;

@Component("certificateEntityToV73PolicyModelConverter")
public class CertificateEntityToV73PolicyModelConverter
        implements AliasAwareConverter<ReadOnlyKeyVaultCertificateEntity, CertificatePolicyModel> {

    private final Supplier<CertificatePolicyModel> modelSupplier;

    public CertificateEntityToV73PolicyModelConverter() {
        this(CertificatePolicyModel::new);
    }

    public CertificateEntityToV73PolicyModelConverter(@NonNull final Supplier<CertificatePolicyModel> modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    @Override
    public CertificatePolicyModel convert(
            @org.springframework.lang.NonNull final ReadOnlyKeyVaultCertificateEntity source,
            @org.springframework.lang.NonNull final URI vaultUri) {
        return mapActiveFields(source, modelSupplier.get(), vaultUri);
    }

    protected CertificatePolicyModel mapActiveFields(
            final ReadOnlyKeyVaultCertificateEntity source,
            final CertificatePolicyModel model,
            final URI vaultUri) {
        model.setId(source.getId().asPolicyUri(vaultUri).toString());
        model.setAttributes(convertPolicyProperties(source));
        model.setIssuer(convertIssuer(source));
        model.setKeyProperties(convertKeyProperties(source));
        model.setSecretProperties(convertSecretProperties(source));
        model.setX509Properties(convertX509Properties(source));
        return model;
    }

    private IssuerParameterModel convertIssuer(final ReadOnlyKeyVaultCertificateEntity source) {
        final IssuerParameterModel issuerParameterModel = new IssuerParameterModel();
        issuerParameterModel.setIssuer(source.getGenerator().getCertAuthorityType().getValue());
        issuerParameterModel.setCertType(null);
        issuerParameterModel.setCertTransparency(source.getGenerator().isEnableTransparency());
        return issuerParameterModel;
    }

    private X509CertificateModel convertX509Properties(final ReadOnlyKeyVaultCertificateEntity source) {
        final X509CertificateModel model = new X509CertificateModel();
        final CertificateCreationInput generator = source.getGenerator();
        model.setSubject(generator.getSubject());
        model.setKeyUsage(generator.getKeyUsage());
        model.setValidityMonths(generator.getValidityMonths());
        model.setExtendedKeyUsage(generator.getExtendedKeyUsage());
        model.setSubjectAlternativeNames(new SubjectAlternativeNames(generator.getDnsNames(), generator.getEmails(), generator.getIps()));
        return model;
    }

    private CertificateSecretModel convertSecretProperties(final ReadOnlyKeyVaultCertificateEntity source) {
        final CertificateSecretModel model = new CertificateSecretModel();
        model.setContentType(source.getGenerator().getContentType().getMimeType());
        return model;
    }

    private CertificatePropertiesModel convertPolicyProperties(final ReadOnlyKeyVaultCertificateEntity source) {
        final CertificatePropertiesModel model = new CertificatePropertiesModel();
        model.setEnabled(source.isEnabled());
        model.setCreatedOn(source.getCreated());
        model.setUpdatedOn(source.getUpdated());
        return model;
    }

    private CertificateKeyModel convertKeyProperties(final ReadOnlyKeyVaultCertificateEntity source) {
        final CertificateCreationInput generator = source.getGenerator();
        final CertificateKeyModel model = new CertificateKeyModel();
        model.setExportable(generator.isExportablePrivateKey());
        model.setReuseKey(generator.isReuseKeyOnRenewal());
        model.setKeySize(generator.getKeySize());
        model.setKeyCurveName(generator.getKeyCurveName());
        model.setKeyType(generator.getKeyType());
        return model;
    }


}
