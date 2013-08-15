class Room < ActiveRecord::Base
  has_many :users
  has_many :missions
  has_many :locations
  has_many :hits
  has_many :results
  belongs_to :owner, :class_name => "User"

  before_save :count_member

  def started?
    self.termination_time == nil
  end

  def count_member
    self.num_user = self.users.size
  end

  def member_hash
    self.users.inject({}){|r, u|
      r[u.id] = u
      r
    }
  end
end
