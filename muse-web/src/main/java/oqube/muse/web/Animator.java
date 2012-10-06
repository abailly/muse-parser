/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Created 4 nov. 07
 */
package oqube.muse.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.lifl.utils.CommandLine;
import fr.lifl.utils.SubstitutableString;

/**
 * A class for invoking methods on an object using data from {@link Parser}
 * instance.
 * 
 * @author nono
 * 
 */
public class Animator {

  TypeHelper th = new TypeHelper();

  private Map<String, String> variables = new HashMap<String, String>();

  private int line;

  private final static Pattern var = Pattern.compile("\\$([0-9]+)");

  private LinkedList<Object> targets;

  protected Object resolve(Parser p) {
    String s = null;
    try {
      s = p.content(variables);
      Matcher m = var.matcher(s);
      if (m.matches())
        return targets.get(Integer.parseInt(m.group(1)));
      else
        return s;
    } catch (IllegalArgumentException e) {
      throw new AnimatorError("Unknown variable in " + p.content(), line);
    } catch (IndexOutOfBoundsException e) {
      throw new AnimatorError("Variable reference  " + s + " does not exist",
          line);
    }
  }

  public Object animateWith(Object target, Parser parser) throws Throwable {
    targets = new LinkedList<Object>();
    this.line = 1;
    targets.add(this);
    if (target != null)
      targets.addFirst(target);
    for (; parser != null; parser = parser.more) {
      String m = (String) resolve(parser);
      if ("".equals(m)) {
        targets = new LinkedList<Object>();
        targets.add(this);
        if (target != null)
          targets.addFirst(target);
      } else {
        List<Object> pars = new ArrayList<Object>();
        for (Parser n = parser.next; n != null; n = n.next) {
          pars.add(resolve(n));
        }
        eval0(m, pars, targets);
        // reset targets list
        if (targets.size() == 1 && target != null)
          targets.addFirst(target);
      }
      line++;
    }
    return targets.getFirst();
  }

  private void eval0(String m, List<Object> pars, LinkedList<Object> targets)
      throws Throwable {
    ListIterator<Object> it = targets.listIterator();
    final Object[] array = pars.toArray(new Object[pars.size()]);
    Object current = null;
    Object res = TypeHelper.UNDEFINED;
    while (it.hasNext() && res == TypeHelper.UNDEFINED) {
      current = it.next();
      res = th.invoke(current, m, array);
      if (res == Void.TYPE || res == null)
        return;
      else if (res != TypeHelper.UNDEFINED) {
        if (current != res)
          targets.addFirst(res);
        return;
      }
    }
    throw new AnimatorError("Cannot invoke " + m + pars.toString(), line);
  }

  public void varIs(String name, String value) {
    variables.put(name, new SubstitutableString(value).instance(variables));
  }

  public void is(String name, String value) {
    varIs(name, value);
  }

  public Object make(String... args) throws Throwable {
    // try to locate class
    Class cls = resolveAsClass(args[0]);
    if (cls == null)
      throw new AnimatorError("Cannot find class " + args[0], line);
    // try to construct instance
    Object o = null;
    if (args.length == 1)
      try {
        o = cls.newInstance();
      } catch (Throwable t) {
        throw new AnimatorError("Cannot create instance of " + cls.getName(),
            line);
      }
    else if (args.length == 2)
      o = th.convert(cls, args[1]);
    else {
      String[] converted = new String[args.length - 1];
      System.arraycopy(args, 1, converted, 0, args.length - 1);
      o = th.convert(cls, converted);
    }
    if (o == null)
      throw new AnimatorError("Cannot create instance of " + cls.getName()
          + " with " + args, line);
    return o;
  }

  public Class resolveAsClass(String clname) {
    String[] packages = { "", "java.util.", "java.lang." };
    for (String s : packages)
      try {
        return Class.forName(s + clname);
      } catch (ClassNotFoundException e1) {
      }
    return null;
  }

  public static void main(String[] args) {
    InputStream is = System.in;
    CommandLine cli = new CommandLine();
    cli.addOptionSingle('f'); // script file
    cli.parseOptions(args);
    if (cli.isSet('f'))
      try {
        is = new FileInputStream((String) cli.getOption('c').getArgument());
      } catch (FileNotFoundException e) {
        System.err.println("cannot find file :" + e);
      }
    try {
      Parser p = new Parser(new BufferedReader(new InputStreamReader(is)));
      Animator a = new Animator();
      a.animateWith(null, p);
    } catch (IOException e) {
      System.err.println("I/O Error while configuring server " + e);
    } catch (Throwable e) {
      System.err.println("Error: " + e);
    }
  }
}
