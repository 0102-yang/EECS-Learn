#include "ll_cycle.h"

#include <stddef.h>

int ll_has_cycle(node* head) {
    /* TODO: Implement ll_has_cycle */
    if (!head) {
        return 0;
    }

    node* fast = head;
    node* slow = head;
    while (1) {
        if (fast->next && fast->next->next) {
            fast = fast->next->next;
        } else {
            return 0;
        }
        slow = slow->next;
        if (fast == slow) {
            return 1;
        }
    }
}
