package com.lufi.terrafying.world;

public class Vector2i {
	public int x;
	public int y;
	
	public final static Vector2i Zero = new Vector2i(0, 0);
	
    // Constructors
    public Vector2i() {
        this.x = 0;
        this.y = 0;
    }
       
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Vector2i vec) {
    	this.x = vec.x;
    	this.y = vec.y;
    }
    
    public Vector2i sub(Vector2i vec) {
    	this.x -= vec.x;
    	this.y -= vec.y;
    	return this;
    }
    
    public Vector2i sub(int x, int y) {
    	this.x -= x;
    	this.y -= y;
    	return this;
    }
    
    public Vector2i add(Vector2i vec) {
    	this.x += vec.x;
    	this.y += vec.y;
    	return this;
    }
    
    public Vector2i add(int x, int y) {
    	this.x += x;
    	this.y += y;
    	return this;
    }
       
    @Override
    public boolean equals(Object other) {
    	if(other == null)
    		return false;
    	if(getClass() != other.getClass())
    		return false;
    	if(this == other)
    		return true;
        return (this.x == ((Vector2i)other).x && this.y == ((Vector2i)other).y);
    }
    
    @Override
    public String toString() {
    	return "(" + this.x + "; " + this.y + ")";
    }
    
    @Override
    public int hashCode() {
    	return this.x * 31 + this.y;
    }
}
