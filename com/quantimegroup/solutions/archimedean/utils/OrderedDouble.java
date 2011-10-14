/*
Archimedean 1.1, a 3D applet/application for visualizing, building, 
transforming and analyzing Archimedean solids and their derivatives.
Copyright 1998, 2011 Raffi J. Kasparian, www.raffikasparian.com.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.quantimegroup.solutions.archimedean.utils;public class OrderedDouble{	public double x = 0, y = 0;		public OrderedDouble( double x, double y ){		this.x = x;		this.y = y;	}	OrderedDouble( double x1, double y1, double x2, double y2 ){		this.x = x2 - x1;		this.y = y2 - y1;	}	public double dot( OrderedDouble v ){		return x*v.x + y*v.y;	}	public OrderedDouble cross(){		return normal();	}	public OrderedDouble normal(){		return new OrderedDouble( -y, x );	}	public double xcos(){		return x/length();	}	public double ycos(){		return y/length();	}	public OrderedDouble plus( OrderedDouble v ){		return new OrderedDouble( x + v.x, y + v.y );	}	public void plusEquals( OrderedDouble v ){		x += v.x;		y += v.y;	}	public OrderedDouble minus( OrderedDouble v ){		return new OrderedDouble( x - v.x, y - v.y );	}	public void minusEquals( OrderedDouble v ){		x -= v.x;		y -= v.y;	}	public OrderedDouble times( double a ){		return new OrderedDouble( a*x, a*y );	}	public void timesEquals( double a ){		x *= a;		y *= a;	}	public OrderedDouble dividedBy( double a ){		return new OrderedDouble( x/a, y/a );	}	public void dividedByEquals( double a ){		x /= a;		y /= a;	}	public double length(){		return Math.sqrt( x*x + y*y );	}	public static OrderedDouble findSecondVector( OrderedDouble v1, double theta, OrderedDouble testv ){	// returns the vector a rad from v1 and N length.  if there are two such vectors	//it returns the one with positive component of testv		OrderedDouble v2 = null;		double a = v1.x;		double b = v1.y;		double c, d;		double roots[];		double N = v1.length();		double A = N*N*Math.cos( theta );		double P, Q;		double epsilon = 0.1;				if( ! isApprox( a, 0, epsilon ) ){			//System.out.println( "route 1" );			P = -b/a;			Q = A/a;			roots = solveQuadratic( 1 + P*P, 2*P*Q, Q*Q - N*N );						d = roots[0];			c = P*d + Q;			v2 = new OrderedDouble( c, d );			if( v2.dot( testv ) < 0 ){				d = roots[1];				c = P*d + Q;				v2 = new OrderedDouble( c, d );			}				}else if( ! isApprox( b, 0, epsilon ) ){			//System.out.println( "route 1" );			P = -a/b;			Q = A/b;			roots = solveQuadratic( 1 + P*P, 2*P*Q, Q*Q - N*N );						c = roots[0];			d = P*c + Q;			v2 = new OrderedDouble( c, d );			if( v2.dot( testv ) < 0 ){				c = roots[1];				d = P*c + Q;				v2 = new OrderedDouble( c, d );			}				}		return v2;	}			public boolean equals( OrderedDouble d ){		if( x == d.x && y == d.y ) return true;		return false;	}	public boolean isApprox( OrderedDouble d, double e ){		if( Math.abs( x - d.x ) <= e && Math.abs( y - d.y ) <= e ) return true;		return false;	}	static boolean between( double x, double a, double b ){		return ( x >= a && x <= b ) || ( x >= b && x <= a );	}	static boolean betweenX( double x, double a, double b ){		return ( x > a && x < b ) || ( x > b && x < a );	}	static double findy( double x1, double y1, double x2, double y2, double x ){		double dx = x2 - x1, dy = y2 - y1;		return ( ( x - x1 )/dx )*dy + y1;	}	static double findx( double x1, double y1, double x2, double y2, double y ){		double dx = x2 - x1, dy = y2 - y1;		return ( ( y - y1 )/dy )*dx + x1;	}	static OrderedDouble solveEquations( double a, double b, double c, double d, double e, double f ){		//ax + by = c		//dx + ey = f		double epsilon = 1;		double denom = a*e - b*d;		if( isApprox( denom, 0, epsilon ) ) return null;		return new OrderedDouble( ( c*e - b*f )/denom, ( a*f - c*d )/denom );	}	static OrderedDouble sectLines( double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4 ){		//line from (x1, y1) through (x2, y2) and line from (x3, y3) through (x4, y4)		//(y2-y1)x + (x1-x2)y = x1(y2-y1) + y1(x1-x2)		double a1 = y2 - y1;		double b1 = x1 - x2;		double c1 = x1*a1 + y1*b1;		double a2 = y4 - y3;		double b2 = x3 - x4;		double c2 = x3*a2 + y3*b2;		return solveEquations( a1, b1, c1, a2, b2, c2 );	}	static OrderedDouble sectLines( OrderedDouble p1, OrderedDouble p2, OrderedDouble p3, OrderedDouble p4 ){		//line from p1 through p2 and line from p3 through p4		return sectLines( p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y );	}	static OrderedDouble sectLineSegments( OrderedDouble p1, OrderedDouble p2, OrderedDouble p3, OrderedDouble p4 ){		//line from p1 through p2 and line from p3 through p4		OrderedDouble d = sectLines( p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y );		if( between( d.x, p1.x, p2.x ) && between( d.x, p3.x, p4.x ) ) return d;		else return null;	}	static boolean isApprox( double a, double b, double e ){		return Math.abs( a - b ) <= e;	}	public static double[] solveQuadratic( double a, double b, double c ){		if( a == 0 ){			System.out.println( "a = 0" );			return null;		}		double d2 = b*b - 4*a*c;		if( d2 < 0 ){			System.out.println( "d2 < 0" );			d2 = 0;			//return null;		}		double d = Math.sqrt( d2 );		double roots[] = { ( -b + d )/( 2*a ), ( -b - d )/( 2*a ) };		return roots;	}	public void print(){		System.out.println( Math.round( x ) + ",\t" + Math.round( y ) );	}	static public OrderedDouble clockwise90( OrderedDouble v ){		return new OrderedDouble( -v.y, v.x );	}}