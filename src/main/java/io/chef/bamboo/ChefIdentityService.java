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

import io.chef.bamboo.persistence.ChefIdentityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tfitch on 6/9/16.
 */
public class ChefIdentityService {
    private static final Logger LOG = LoggerFactory.getLogger(ChefIdentityService.class);

    // TODO: probably make this for Bandana?
    private final ChefIdentityRepository chefIdentityRepository;

    public ChefIdentityService (ChefIdentityRepository chefIdentityRepository) {
        this.chefIdentityRepository = chefIdentityRepository;
    }

    public ChefIdentity getChefIdentity(String idName) {
        return chefIdentityRepository.getChefIdentity(idName);
    }

    // meets signature requirements for TemplateRender.render
    public Map<String, Object> getChefIdentityMap(String idName) {
        Map<String, Object> template = new HashMap<String, Object>();

        template.put("chefIdentity", chefIdentityRepository.getChefIdentity(idName));

        return template;
    }

    public void saveChefIdentity(ChefIdentity chefIdentity) {
        chefIdentityRepository.saveChefIdentity(chefIdentity);
    }

    public void deleteChefIdentity(String idName) {
        chefIdentityRepository.deleteChefIdentity(idName);
    }
}
