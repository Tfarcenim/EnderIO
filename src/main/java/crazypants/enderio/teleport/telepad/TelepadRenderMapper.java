package crazypants.enderio.teleport.telepad;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;

import org.apache.commons.lang3.tuple.Pair;

import crazypants.enderio.render.BlockStateWrapper;
import crazypants.enderio.render.EnumRenderMode;
import crazypants.enderio.render.IRenderMapper;

public class TelepadRenderMapper implements IRenderMapper.IRenderLayerAware {

  private static final EnumRenderMode GLASS_TOP_MODEL = EnumRenderMode.FRONT_ON;
  private static final EnumRenderMode FULL_MODEL = EnumRenderMode.FRONT_EAST;
  private static final EnumRenderMode SINGLE_MODEL = EnumRenderMode.FRONT;
  private static final EnumRenderMode SINGLE_MODEL_INVENTORY = EnumRenderMode.FRONT_SOUTH;
  private static final EnumRenderMode NULL_MODEL = EnumRenderMode.FRONT_ON_SOUTH;

  public TelepadRenderMapper() {
  }

  protected Pair<List<IBlockState>, List<IBakedModel>> render(IBlockState state, IBlockAccess world, BlockPos pos, TileTelePad tileEntity) {
    List<IBlockState> states = new ArrayList<IBlockState>();
    
    if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.TRANSLUCENT) {
      if (!tileEntity.inNetwork() || !tileEntity.isMaster()) {
        states.add(state.withProperty(EnumRenderMode.RENDER, NULL_MODEL));
      } else {
        states.add(state.withProperty(EnumRenderMode.RENDER, GLASS_TOP_MODEL));
      }
    } else if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID) {
      if (!tileEntity.inNetwork()) {
        states.add(state.withProperty(EnumRenderMode.RENDER, SINGLE_MODEL));
      } else if (tileEntity.isMaster()) {
        states.add(state.withProperty(EnumRenderMode.RENDER, FULL_MODEL));
      } else {
        states.add(state.withProperty(EnumRenderMode.RENDER, NULL_MODEL));
      }
    } else {
      return null;
    }

    return Pair.of(states, null);
  }

  @Override
  public Pair<List<IBlockState>, List<IBakedModel>> mapBlockRender(BlockStateWrapper state, IBlockAccess world, BlockPos pos) {
    if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.CUTOUT) {
      TileEntity tileEntity = state.getTileEntity();

      if (tileEntity instanceof TileTelePad) {
        return render(state.getState(), world, pos, (TileTelePad) tileEntity);
      }
    }
    return null;
  }

  @Override
  public Pair<List<IBlockState>, List<IBakedModel>> mapItemRender(Block block, ItemStack stack) {
    List<IBlockState> states = new ArrayList<IBlockState>();
    states.add(block.getStateFromMeta(stack.getMetadata()).withProperty(EnumRenderMode.RENDER, SINGLE_MODEL_INVENTORY));
    return Pair.of(states, null);
  }

  @Override
  public List<IBlockState> mapOverlayLayer(BlockStateWrapper state, IBlockAccess world, BlockPos pos) {
    return null;
  }

  @Override
  public Pair<List<IBlockState>, List<IBakedModel>> mapItemPaintOverlayRender(Block block, ItemStack stack) {
    return null;
  }

}