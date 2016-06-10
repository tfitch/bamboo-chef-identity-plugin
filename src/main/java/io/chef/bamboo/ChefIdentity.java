/* The MIT License (MIT)
 *
 * Copyright (c) 2016 Tyler Fitch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.chef.bamboo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Represents a Chef Identity
 * Which is a pem.key file used for auth against a Chef Server
 *
 * @author tfitch on 6/9/16.
 */
public class ChefIdentity implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(ChefIdentity.class);

    private final String idName;
    // TODO: big ol' todo, find the equivalent of Jenkins' Secret class to secure the pemKey and knifeRb
    private String pemKey;
    private String knifeRb;

    public ChefIdentity() {
        this.idName = null;
        this.pemKey = null;
        this.knifeRb = null;
    }

    public ChefIdentity(String idName, String pemKey, String knifeRb) {
        this.idName = idName;
        // TODO: more secret management, this time loading
//        if (this.pemKey == null) this.pemKey = Secret.fromString(pemKey);
//        if (this.knifeRb == null) this.knifeRb = Secret.fromString(knifeRb);
    }

    public String getIdName() {
        return idName;
    }

    public String getPemKey() {
        return pemKey;
        // TODO: descramble when a Secret
    }

    public String getKnifeRb() {
        return knifeRb;
        // TODO: again, descramble when a Secret
    }
}
