// MongoException.java

/**
 *      Copyright (C) 2008 10gen Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.mongodb;

import org.bson.*;

public class MongoException extends RuntimeException {

    public MongoException( String msg ){
        super( msg );
        _code = -3;
    }

    public MongoException( int code , String msg ){
        super( msg );
        _code = code;
    }

    public MongoException( String msg , Throwable t ){
        super( msg , _massage( t ) );
        _code = -4;
    }

    public MongoException( int code , String msg , Throwable t ){
        super( msg , _massage( t ) );
        _code = code;
    }
    
    public MongoException( BSONObject o ){
        this( ServerError._getCode( o ) , _getMsg( o ) );
    }

    static String _getMsg( BSONObject o ){
        Object e = o.get( "$err" );
        if ( e == null )
            e = o.get( "err" );
        if ( e == null )
            return "UNKNOWN";
        return e.toString();
    }


    static Throwable _massage( Throwable t ){
        if ( t instanceof Network )
            return ((Network)t)._ioe;
        return t;
    }

    public static class Network extends MongoException {

        Network( String msg , java.io.IOException ioe ){
            super( -2 , msg , ioe );
            _ioe = ioe;
        }

        Network( java.io.IOException ioe ){
            super( ioe.toString() , ioe );
            _ioe = ioe;
        }
        
        final java.io.IOException _ioe;
    }

    public static class DuplicateKey extends MongoException {
        DuplicateKey( int code , String msg ){
            super( code , msg );
        }
    }

    public int getCode(){
        return _code;
    }

    final int _code;
}
