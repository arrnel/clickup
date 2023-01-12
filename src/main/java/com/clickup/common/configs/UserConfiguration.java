package com.clickup.common.configs;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:configs/user.properties")
public interface UserConfiguration extends Config{

    @Separator(";")
    @Key("TOKENS[]")
    @DefaultValue("pk_48448519_Y6EIQTP6OJ15EDX6XNE01HHNGEXTP35G;" +
            "  pk_48448526_JTHYJC93R0H5XWYOCNWE84L5PPIX4Z41;" +
            "  pk_48448529_TVYU4T4GL8VZZROQ5CMQRQF73TLZN45C;" +
            "  pk_48448530_HCWTPFDXGZBXKO5LMIM0RUQYQKF71CAI;" +
            "  pk_48448532_V2P1GRCQLFFOY2L667EQ0FUZFVTFY2P6;" +
            "  pk_48448534_B57SVKZ93R3GRCXE7F5RM0L11MWLTMW8;" +
            "  pk_48448536_ZFVA04B18HV9D265YNLNOLX3JXYKE20W;" +
            "  pk_48448539_0XZ9SPHQKQGFI0HKPI5D8L9KF4WL4ZWW;" +
            "  pk_48448540_4L68ZHZBEQDDBFDT6JMJQ3GYHE6S9GQ7;" +
            "  pk_48448544_6S5LV0716I42KKENDKKZFDTLBUMGVBRY;" +
            "  pk_48448549_8CUHUUJ2AJXU11ZKILQ9K5JRZ46JGOEO;" +
            "  pk_48448550_5Q6HIL2JN5S9D0EZSUTA7AGNZS6WY8W1;" +
            "  pk_48448556_MC3060KJDF1KZZNMV90OJLN8YM90D1UI;" +
            "  pk_48448566_CD9M3DL5VQI42PRXFJOJ76UGUZJ9ILU7;" +
            "  pk_48448568_7PVK1VLOFAB930QJLL02G9O1NYCGZGRM")
    String[] tokens();

    @Key("TOKEN_FREE_PLAN")
    @DefaultValue("pk_49695307_1UXWZGL7MCVITFOQ0RR0X4FDXFAKU6IH")
    String tokenFreePlan();

    @Key("TOKEN_PAID_PLAN")
    @DefaultValue("pk_48397325_MUDSR0UW5SCOZUBZJZSR5COO7ZR09VT2")
    String tokenPaidPlan();

    @Key("NOT_MY_TEAM_ID")
    @DefaultValue("24580397")
    String getNotMyTeamId();

    @Key("NOT_MY_SPACE_ID")
    @DefaultValue("48568794")
    String getNotMySpaceId();

    @Key("NOT_MY_FOLDER_ID")
    @DefaultValue("90040090361")
    String getNotMyFolderId();

    @Key("NOT_MY_LIST_ID")
    @DefaultValue("900500164997")
    String getNotMyListId();

    @Key("PATH_TO_USERS_FILE")
    @DefaultValue("resources/files/users.csv")
    String pathToUsersFile();
}
