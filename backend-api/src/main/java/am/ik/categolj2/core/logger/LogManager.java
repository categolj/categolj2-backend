/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    /**
     * Returns a Logger with the name of the calling class.
     *
     * @return The Logger for the calling class.
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger(getClassName(2));
    }

    /**
     * Gets the class name of the caller in the current stack at the given {@code depth}.
     *
     * @param depth a 0-based index in the current stack.
     * @return a class name
     */
    private static String getClassName(final int depth) {
        return new Throwable().getStackTrace()[depth].getClassName();
    }
}
