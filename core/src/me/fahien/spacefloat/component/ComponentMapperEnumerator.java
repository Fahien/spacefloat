package me.fahien.spacefloat.component;

import com.badlogic.ashley.core.ComponentMapper;

/**
 * The {@link ComponentMapper}s
 *
 * @author Fahien
 */
public class ComponentMapperEnumerator {
	public static ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
	public static ComponentMapper<EnergyComponent> energyMapper = ComponentMapper.getFor(EnergyComponent.class);
	public static ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	public static ComponentMapper<HurtComponent> hurtMapper = ComponentMapper.getFor(HurtComponent.class);
	public static ComponentMapper<RechargeComponent> rechargeMapper = ComponentMapper.getFor(RechargeComponent.class);
	public static ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
	public static ComponentMapper<GravityComponent> gravityMapper = ComponentMapper.getFor(GravityComponent.class);
	public static ComponentMapper<AccelerationComponent> accelerationMapper = ComponentMapper.getFor(AccelerationComponent.class);
	public static ComponentMapper<ReactorComponent> reactorMapper = ComponentMapper.getFor(ReactorComponent.class);
	public static ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	public static ComponentMapper<RigidbodyComponent> rigidMapper = ComponentMapper.getFor(RigidbodyComponent.class);
}
