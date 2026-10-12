package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new Follower(null, null, null);
    }
}
