package track.lessons.lesson3;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MyLinkedListTest {

    @Test(expected = NoSuchElementException.class)
    public void emptyList() throws Exception {
        List list = new MyLinkedList();
        Assert.assertTrue(list.size() == 0);
        list.get(0);
    }

    @Test
    public void listAdd() throws Exception {
        List list = new MyLinkedList();
        list.add(1);

        Assert.assertTrue(list.size() == 1);
    }

    @Test
    public void listAddRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        list.remove(1);
        Assert.assertEquals(3, list.get(1));
        Assert.assertEquals(1, list.get(0));

        list.remove(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listRemove() throws Exception {
        List list = new MyLinkedList();
        list.add(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyQueue() throws Exception {
        Queue queue = new MyLinkedList();
        queue.dequeue();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyStack() throws Exception {
        Stack stack = new MyLinkedList();
        stack.pop();
    }

    @Test
    public void queueAddRemove() throws Exception {
        Queue queue = new MyLinkedList();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(2, queue.dequeue());
        Assert.assertEquals(3, queue.dequeue());
    }

    @Test
    public void stackAddRemove() throws Exception {
        Stack stack = new MyLinkedList();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        Assert.assertEquals(3, stack.pop());
        Assert.assertEquals(2, stack.pop());
        Assert.assertEquals(1, stack.pop());
    }

    private void queueTestNOperations(int n)
    {
        MyLinkedList queue = new MyLinkedList();
        Random r=new Random();
        LinkedList<Integer> goodQueue=new LinkedList<Integer>();
        int num;
        for(int i=0;i<n;i++)
        {
            //enqueue element if queue is empty, or on a 2/3 chance
            if(queue.size() == 0 || r.nextInt(3)<2)
            {
                num=r.nextInt(100);
                queue.enqueue(num);
                goodQueue.add(num);
            }
            else //dequeue
            {
                Assert.assertTrue("Queues differ on dequeue",goodQueue.remove()==queue.dequeue());
            }
            Assert.assertEquals("goodQueue and queue do not match on isEmpty()",goodQueue.isEmpty(),queue.size() == 0);
        }

        while(!(queue.size() == 0))
        {
            Assert.assertTrue("goodQueue is empty but queue isn't",!goodQueue.isEmpty());
            Assert.assertTrue("End dequeues do not match",goodQueue.remove()==queue.dequeue());
        }
        Assert.assertTrue("queue is empty but goodQueue isn't",goodQueue.isEmpty());
    }

    @Test
    public void queueTest10()
    {
        queueTestNOperations(10);
    }

    @Test
    public void queueTest100()
    {
        queueTestNOperations(100);
    }

    @Test
    public void queueTest10000()
    {
        queueTestNOperations(10000);
    }

    @Test
    public void queueTestMillion()
    {
        queueTestNOperations(1000000);
    }

    private void stackTestNOperations(int n)
    {
        MyLinkedList stack = new MyLinkedList();
        Random r=new Random();
        LinkedList<Integer> goodStack=new LinkedList<Integer>();
        int num;
        for(int i=0;i<n;i++)
        {
            //push element if stack is empty, or on a 2/3 chance
            if(stack.size() == 0 || r.nextInt(3)<2)
            {
                num=r.nextInt(100);
                stack.push(num);
                goodStack.push(num);
            }
            else //pop
            {
                Assert.assertTrue("Stacks differ on pop",goodStack.pop()==stack.pop());
            }
            Assert.assertEquals("goodStack and stack do not match on isEmpty()",goodStack.isEmpty(),stack.size() == 0);
        }

        while(!(stack.size() == 0))
        {
            Assert.assertTrue("goodStack is empty but stack isn't",!goodStack.isEmpty());
            Assert.assertTrue("End pops do not match",goodStack.remove()==stack.pop());
        }
        Assert.assertTrue("stack is empty but goodStack isn't",goodStack.isEmpty());
    }

    @Test
    public void stackTest10()
    {
        stackTestNOperations(10);
    }

    @Test
    public void stackTest100()
    {
        stackTestNOperations(100);
    }

    @Test
    public void stackTest10000()
    {
        stackTestNOperations(10000);
    }

    @Test
    public void stackTestMillion()
    {
        stackTestNOperations(1000000);
    }
}