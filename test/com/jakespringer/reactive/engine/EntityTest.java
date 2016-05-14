package com.jakespringer.reactive.engine;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntityTest {
	public Cell<Object> objectCell = new Cell<>();
	
	public class TestingEntity extends Entity {

		public boolean constructed = false;
		public boolean destroyed = false;
		
		@Override
		protected void onConstruct() {
			constructed = true;
			objectCell.map(x -> x);
			objectCell.send(x -> {});
		}

		@Override
		protected void onDestroy() {
			destroyed = true;
		}	
	}
	
	@Test
	public void testDestroyingEntity() {
		int numChildren = objectCell.__debugGetNumChildren();
		TestingEntity e = Entity.construct(new TestingEntity());
		assertEquals(true, e.constructed);
		assertEquals(false, e.destroyed);
		assertEquals(2, objectCell.__debugGetNumChildren()-numChildren);
		Entity.destroy(e);
		assertEquals(true, e.constructed);
		assertEquals(true, e.destroyed);
		assertEquals(0, objectCell.__debugGetNumChildren()-numChildren);
	}
}
