package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.utils.Faceable;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class BlockStairs extends BlockSolidMeta implements Faceable {

    private static final short[] faces = new short[]{2, 1, 3, 0};

    protected BlockStairs(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if ((this.getDamage() & 0x04) > 0) {
            return new AxisAlignedBB(
                    this.x,
                    this.y + 0.5,
                    this.z,
                    this.x + 1,
                    this.y + 1,
                    this.z + 1
            );
        } else {
            return new AxisAlignedBB(
                    this.x,
                    this.y,
                    this.z,
                    this.x + 1,
                    this.y + 0.5,
                    this.z + 1
            );
        }
    }

    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(faces[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        if ((fy > 0.5 && face != BlockFace.UP) || face == BlockFace.DOWN) {
            this.setDamage(this.getDamage() | 0x04); //Upside-down stairs
        }
        this.getLevel().setBlock(block, this, true, true);

        return true;
    }

    @Override
    public Item toItem() {
        Item item = super.toItem();
        item.setDamage(0);
        return item;
    }

    @Override
    public boolean collidesWithBB(AxisAlignedBB bb) {
        int damage = this.getDamage();
        int side = damage & 0x03;
        double f = 0;
        double f1 = 0.5;
        double f2 = 0.5;
        double f3 = 1;
        if ((damage & 0x04) > 0) {
            f = 0.5;
            f1 = 1;
            f2 = 0;
            f3 = 0.5;
        }

        if (bb.intersectsWith(new AxisAlignedBB(
                this.x,
                this.y + f,
                this.z,
                this.x + 1,
                this.y + f1,
                this.z + 1
        ))) {
            return true;
        }


        if (side == 0) {
            return bb.intersectsWith(new AxisAlignedBB(
                    this.x + 0.5,
                    this.y + f2,
                    this.z,
                    this.x + 1,
                    this.y + f3,
                    this.z + 1
            ));
        } else if (side == 1) {
            return bb.intersectsWith(new AxisAlignedBB(
                    this.x,
                    this.y + f2,
                    this.z,
                    this.x + 0.5,
                    this.y + f3,
                    this.z + 1
            ));
        } else if (side == 2) {
            return bb.intersectsWith(new AxisAlignedBB(
                    this.x,
                    this.y + f2,
                    this.z + 0.5,
                    this.x + 1,
                    this.y + f3,
                    this.z + 1
            ));
        } else if (side == 3) {
            return bb.intersectsWith(new AxisAlignedBB(
                    this.x,
                    this.y + f2,
                    this.z,
                    this.x + 1,
                    this.y + f3,
                    this.z + 0.5
            ));
        }

        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
