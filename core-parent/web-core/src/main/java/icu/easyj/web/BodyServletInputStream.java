/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import org.springframework.lang.NonNull;

/**
 * @author wangliang181230
 */
public class BodyServletInputStream extends ServletInputStream {

	private final InputStream delegate;

	public BodyServletInputStream(byte[] body) {
		this.delegate = new ByteArrayInputStream(body);
	}


	//region Override

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException("'" + this.getClass().getName() + "'不支持`setReadListener`方法.");
	}

	@Override
	public int read() throws IOException {
		return this.delegate.read();
	}

	@Override
	public int read(@NonNull byte[] b, int off, int len) throws IOException {
		return this.delegate.read(b, off, len);
	}

	@Override
	public int read(@NonNull byte[] b) throws IOException {
		return this.delegate.read(b);
	}

	@Override
	public long skip(long n) throws IOException {
		return this.delegate.skip(n);
	}

	@Override
	public int available() throws IOException {
		return this.delegate.available();
	}

	@Override
	public void close() throws IOException {
		this.delegate.close();
	}

	@Override
	public synchronized void mark(int readLimit) {
		this.delegate.mark(readLimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		this.delegate.reset();
	}

	@Override
	public boolean markSupported() {
		return this.delegate.markSupported();
	}

	//endregion
}