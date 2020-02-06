package com.godavari.appsnest.fms.dao.model;

import com.godavari.appsnest.fms.dao.daofactory.DaoFactory;
import com.godavari.appsnest.fms.dao.hashing.HashingMD5;
import com.godavari.appsnest.fms.dao.interfaces.IMD5ModelDao;
import com.godavari.appsnest.fms.dao.utility.Constants;
import lombok.extern.log4j.Log4j;

import java.util.Arrays;

@Log4j
public class MD5Model {

    public static final int SALT_LENGTH = 5;

    public static void main(String[] args) {
        //MD5Model md5Model = new MD5Model("1231231231");
        //md5Model.generateSecurePassword();
        //md5Model.save(InputPara.USER_FILE_GENERATED_NAME);
        //new MD5Model("apps nest home for software development").generateSecurePassword();

        generateAndSaveAdmin();
        generateAndSaveUserGenerated();

    }

    private static void generateAndSaveAdmin() {
        MD5Model md5Model = new MD5Model("apps nest home for software development");
        md5Model.generateSecurePassword();
        md5Model.save(Constants.ADMIN_FILE_NAME);
    }

    private static void generateAndSaveUserGenerated() {
        MD5Model md5Model = new MD5Model(String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
        md5Model.generateSecurePassword();
        md5Model.save(Constants.USER_FILE_GENERATED_NAME);
    }

    private String passwordToHash;
    private byte[] salt;
    private String securePassword;

    public MD5Model(String passwordToHash) {
        this.passwordToHash = passwordToHash;
    }

    public MD5Model(String passwordToHash, byte[] salt) {
        this(passwordToHash);
        this.salt = salt;
    }

    public String getPasswordToHash() {
        return passwordToHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public String getSecurePassword() {
        return securePassword;
    }

    public void generateSecurePassword() {
        try {
            salt = HashingMD5.getSalt(SALT_LENGTH);
            securePassword = HashingMD5.getSecurePassword(passwordToHash, salt);

        } catch (Exception e) {
            log.error("generateSecurePassword", e);
        }
    }

    public boolean checkSecurePasswordCorrect() {
        try {
            String securePasswordGenerated = HashingMD5.getSecurePassword(passwordToHash, salt);
            if (securePasswordGenerated.equals(securePassword)) {
                return true;
            }
        } catch (Exception e) {
            log.error("checkSecurePasswordCorrect", e);
        }
        return false;
    }

    public boolean checkSecurePassword(MD5Model userMD5Model) {
        try {
            String securePasswordGenerated = HashingMD5.getSecurePassword(userMD5Model.passwordToHash, userMD5Model.salt);
            userMD5Model.securePassword = securePasswordGenerated;
            if (securePassword.equals(securePasswordGenerated)) {
                return true;
            }
        } catch (Exception e) {
            log.error("checkSecurePassword", e);
        }
        return false;
    }

    public void save(String fileName) {
        getMD5ModelDao().save(this, fileName);
    }

    public void delete(String fileName) {
        getMD5ModelDao().delete(fileName);
    }

    public static MD5Model getObjectByName(String fileName) {
        return getMD5ModelDao().getObjectByName(fileName);
    }

    public static MD5Model getObjectByNameFromResource(String fileName) {
        return getMD5ModelDao().getObjectByNameFromResource(fileName);
    }

    private static IMD5ModelDao getMD5ModelDao() {
        return DaoFactory.getDatabase().getMD5ModelDao();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(log.getName() + "\n");
        stringBuilder.append("passwordToHash" + " : " + passwordToHash + "\n")
                .append("salt" + " : " + Arrays.toString(salt) + "\n")
                .append("securePassword" + " : " + securePassword + "\n");
        return stringBuilder.toString();
    }
}
